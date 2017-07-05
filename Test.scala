/*
val vec = Vector(1,2,3)
println(vec(1))

val newVec = Vector(4,5,6)
val concatVec = vec ++ newVec

println(concatVec)

val c = concatVec.updated(0, 10)
println(c)

val d = c.filter(_ > 5)
println(d)



import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
// implicit val ec = ExecutionContext.global

val s = "Hello"
val f: Future[String] = Future {
    s + " future!"
}

println(f)

f foreach {
    msg => println(msg)
}

f onSuccess {
    case result => println("Success:" + result)
}

f onFailure {
    case error => println("Error:" + error)
}

case class Item(name: String, age: Int)

val newItem = Item("john", 1)
println(newItem)
val dao: List[Item] = List(newItem, Item("doe", 2))
println(dao)

val out = dao.filter((_.name == "john"))
println(out)


val t = dao ++ List(Item("scala", 2))
println(t)]

*/