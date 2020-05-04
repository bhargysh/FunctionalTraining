package ft.typesafety

/**
  * Use pattern matching and recursion.  No vars, no loops, no overriding.
  *
  * `Option` is an implementation of optional functionality.
  *
  * We've made these exercises to give you greater insight into how an optional pattern
  * might work in a functional language.
  *
  * When you see `Option` think: "It may exist, or it may not"
  *
  * There are two ways to construct an `Option`:
  *
  *   `Some()` represents something that exists
  *
  *   `None` represents something that doesn't exist
  *
  * We use `Option` in situations where there isn't certainty that a meaningful
  * value will be returned to us.
  *
  *   The `get()` method on the key to value store `Map` is a great example of this.
  *
  *     We expect `get()` to take a key and give us a value in return.
  *
  *     But what happens when our Map doesn't know about the key we gave it?
  *
  *   A Map here is the same as in any other language,
  *   we just need to tell it about the types we're working with.
  *
  *     This is the type of the key
  *                      |
  *                      |     This is the type of the value
  *                      |      |
  *                      |      |
  *     val myMap = Map[Int, String]( 1 -> "one", 2 -> "two, ...)
  *
  *
  *   When we call `get()` on Map we will always get back an `Option` type
  *
  *     myMap.get(1) = Some("one")  //The value exists and it's the string "one"
  *
  *     myMap.get(0) = None      //The value doesn't exist so we get None
  *
  *   `Some("one")` and `None` are both of the type Option
  *
  *   Since `Some` and `None` are the same type we can pattern match on them!
  *
  *   We can have one set of logic when we get Some back and a different set
  *   of logic when we get `None` back!
  *
  *   val mightBeSomething: Option[String] = myMap.get(3)
  *
  *   val result: String = mightBeSomething match {
  *     case Some(string) => "I got a String back!"
  *     case None => "I got None back"
  *   }
  *
  * Good luck!
  *
  */

object OptionalExercises1 {

  val config = Map[String, String]("host" -> "rea.com", "port" -> "8080")

  def getFromConfig(key: String): Option[String] = {
    if (key.isEmpty) {
      None
    } else {
      config.get(key)
    }
  }

  def lengthOfHost(): Option[Int] = config.get("host").map(str => str.length)

  def portPlus1000(): Option[Int] =
    config.get("port").map(str => str.toInt + 1000)
//  WHAT MAP DOES UNDER THE HOOD
//  def portPlus1000(): Option[Int] = config.get("port") match {
//      case Some(value) => Some(value.toInt + 1000)
//      case None        => None
//    }
//  }
}

object OptionalExercises2 {

  val hosts = Map("host1" -> "rea.com", "host2" -> "test.rea.com", "host3" -> "netflix.com")
  val envs = Map("rea.com" -> "prod", "test.rea.com" -> "test", "amazon.com" -> "stage")

  // Should return the host string if successful or "couldn't resolve" if unsuccessful
//  def getEnvForHost(host: String): String = {
//    val hostName = hosts.get(host)
//    hostName match {
//      case Some(value) if envs.contains(value) => envs.get(value) match {
//          case Some(x) => x
//          case _    => "couldn't resolve"
//        }
//      case _        => "couldn't resolve"
//    }
//  }
  def getEnvForHost(key: String): String = {
    hosts.get(key).flatMap(host => envs.get(host)).getOrElse("couldn't resolve")
  }

  // See how many ways you can implement this.
  // Will either return "Connected to <rea host>" or "not connected"
  def connectToReaHostsOnly(host: String): String = {
    val domain = hosts.getOrElse(host, "not connected")
    if(domain.contains("rea")) {
      createConnection(domain)
    } else {
      "not connected"
    }
  }

  def createConnection(domain: String): String = s"connected to $domain"
}

/**
  * Here we make the trait `Maybe`, which is our version of `Option`
  *
  * `Just` has the same behavior as `Some`
  * `Nothing` has the same behavior as `None`
  *
  * We use this exercise to illustrate that we can create our own optional behavior
  * with just a few functions.
  *
  */

object OptionalExercises3 {

  sealed trait Maybe[+A]

  case class Just[A](get: A) extends Maybe[A]

  case object Nothing extends Maybe[Nothing]

  def flatMap[A, B](m: Maybe[A])(f: A => Maybe[B]): Maybe[B] = m match {
      case Just(value) => f(value)
      case Nothing   => Nothing
    }

  def map[A, B](m: Maybe[A])(f: A => B): Maybe[B] = m match {
      case Just(value) => Just(f(value))
      case Nothing   => Nothing
    }

  def fold[A, B](m: Maybe[A], default: => B, f: A => B): B = m match {
      case Just(value) => f(value)
      case Nothing   => default
    }

  def orElse[A](m: Maybe[A], otherwise: => Maybe[A]): Maybe[A] = m match {
      case Just(value) => Just(value)
      case Nothing   => otherwise
    }

  def orSome[A](m: Maybe[A], default: => A): A = m match {
      case Just(value) => value
      case Nothing   => default
    }

//  def map2[A, B, C](f: (A, B) => C)(m1: Maybe[A], m2: Maybe[B]): Maybe[C] = m1 match {
//      case Just(a) => m2 match {
//            case Just(b) => Just(f(a, b))
//            case Nothing => Nothing
//          }
//      case Nothing   => Nothing
//  }
  def map2[A, B, C](f: (A, B) => C)(m1: Maybe[A], m2: Maybe[B]): Maybe[C] = {
    def aToC(a: A): Maybe[C] = {
      map(m2)(b => f(a,b))
    }
  flatMap(m1)(aToC)
  }

  // List(Just(1), Just(2)) --> Just(List(1, 2))
  def sequence[A](l: List[Maybe[A]]): Maybe[List[A]] = {
    l match {
      case Nil          => Nothing
      case ::(head, tl) => head match {
        case Just(get) => ??? // flatMap(Just(List(get)))(sequence(tl)) // idk aye
        case Nothing   => Nothing
      }
    }
  }

  def ap[A, B](m1: Maybe[A], m2: Maybe[A => B]): Maybe[B] = {
    def aToB(ab: A => B): Maybe[B] = ???
    flatMap(m1)(???)

    m2 match {
        case Just(a) => aToB(a) // a = A => B, we need a func that takes a and gives back Maybe[B]
        case Nothing   => ???
    }
  }
}
