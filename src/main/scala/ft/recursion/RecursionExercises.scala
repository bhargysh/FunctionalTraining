package ft.recursion

// Taken from http://tmorris.net/posts/scala-exercises-for-beginners/index.html

/**
 * Ok here are the rules.
 *
 * You can't use any of the standard list functions, like `map`, `filter`, `flatMap`, `append`, `:::`, `:+`, etc.
 * 
 * But you can always use `::` to construct a new list by prepending an element to another list.
 *
 * You CAN and are encouraged to use the solutions from the exercises below to solve the harder
 * ones towards the end.
 *
 * Keep an eye out for repetition and similarities between your answers.
 *
 * REMEMBER: Follow the types, they almost always guide you to the solution.  If it compiles and looks a little
 * too simple, it's probably correct.  As Sherlock Holmes once said, "Each one is suggestive, together they are
 * most certainly conclusive."
 *
 * See if you can make your solution tail recursive, where possible.
 *
 */

object RecursionExercises {

  def plusOne(n: Int) = n + 1

  def minusOne(n: Int) = n - 1

  // Add two non-negative Integers together.  You are only allowed to use plusOne and minusOne above
  def add(a: Int, b: Int): Int = a match {
    case 0 => b // same as minusOne(plusOne(b))
    case _ => add(minusOne(a), plusOne(b))
  }

  // You are not permitted to use any list functions such as map, flatMap, ++, flatten etc
//  def sum(l: List[Int]): Int = l match {
//    case Nil => 0
//    case num :: tail => num + sum(tail)
//  }
  def sum(l: List[Int]): Int = {
    def innerSum(l: List[Int], acc: Int): Int = l match {
        case Nil          => acc
        case ::(head, tl) => innerSum(tl, head + acc)
      }
    innerSum(l, acc = 0)
  }

  //Again no list functions are permitted for the following
//  def length[A](x: List[A]): Int = x match {
//    case Nil => 0
//    case _ :: tail => 1 + length(tail)
//  }
  def length[A](x: List[A]): Int = {
    def innerLength(x: List[A], acc: Int): Int = x match {
        case Nil          => acc
        case ::(_, tl) => innerLength(tl, acc + 1)
      }
    innerLength(x, acc = 0)
  }

  // Do you notice anything similar between sum and length? Hmm...

  // Mapping over a list.  You are given a List of type A and a function converting an A to a B
  // and you give back a list of type B.  No list functions allowed!
  def map[A, B](x: List[A], f: A => B): List[B] = x match {
    case Nil => Nil
    case head :: tail => f(head) :: map(tail, f)
  }

  // Given a function from A => Boolean, return a list with only those item where the function returned true.
  def filter[A](x: List[A], f: A => Boolean): List[A] = {
    if (x.isEmpty) List()
    else if (f(x.head)) x.head :: filter(x.tail, f)
    else filter(x.tail, f)
  }

  // This pattern should be familiar by now... psst... look at add.
  def append[A](x: List[A], y: List[A]): List[A] = x match {
    case Nil => y
    case head :: tail => head :: append(tail, y)
  }

  // Flatten a list of lists to a single list.  Remember you can't use list.flatten.  Can you use a previous
  // solution to solve this one?
  def flatten[A](x: List[List[A]]): List[A] = x match {
    case Nil => List()
    case head :: tail => head ::: flatten(tail)
  }

  // Follow the types.  You've done a great job getting here. Follow the types.
  def flatMap[A, B](x: List[A], f: A => List[B]): List[B] = x match {
    case Nil => List()
        case head :: tail => f(head) ::: flatMap(tail, f)
  }

  // Maximum of the empty list is 0
  def maximum(x: List[Int]): Int = {
    if (x.isEmpty) {
      0
    }
    else if (x.head >= maximum(x.tail)) {
      x.head
    } else {
      maximum(x.tail)
    }
  }

  // Reverse a list
  def reverse[A](x: List[A]): List[A] = x match {
    case Nil => x
    case head :: tail => reverse(tail) ::: List(head)
  }
}
