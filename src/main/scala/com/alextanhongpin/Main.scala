package com.alextanhongpin

import akka.actor.ActorSystem
import akka.Done
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import akka.stream.ActorMaterializer

import scala.io.StdIn
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success}

object WebServer {

    // domain model
    var dao: List[Item] = List(Item("john", 1))
    final case class Item(name: String, id: Long)
    final case class Order(items: List[Item])

    // formats for unmarshalling and marshalling
    implicit val itemFormat = jsonFormat2(Item)
    implicit val orderFormat = jsonFormat1(Order)

    // (fake) async database query api
    def fetchItem(itemId: Long): Future[Option[Item]] = {
        val out = dao.filter(_.id == itemId)
        // return Option(out)
        return Future { Option(out(0)) }
    }

    def saveOrder(order: Order): Future[Done] = {
        dao = dao ++ order.items
        // return Success()
        return Future { Done }
    }

    def main(args: Array[String]) {
        implicit val system = ActorSystem("my-system")
        implicit val materializer = ActorMaterializer()
        implicit val executionContext = system.dispatcher

        val route: Route =
            get {
                pathPrefix("item" / LongNumber) { id =>
                    // There might be no item for a given id
                    val maybeItem: Future[Option[Item]] = fetchItem(id)

                    onSuccess(maybeItem) {
                        case Some(item) => complete(item)
                        case None => complete(StatusCodes.NotFound)
                    }
                } 
            } ~
            post {
                path("create-order") {
                    entity(as[Order]) { order =>
                        val saved: Future[Done] = saveOrder(order)
                        onComplete(saved) { done =>
                            complete("order created")
                        }
                    }
                }
            }

        val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

        println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

        StdIn.readLine() // Let it run until user presses return
        bindingFuture
            .flatMap(_.unbind())
            .onComplete(_ => system.terminate())
    }
}