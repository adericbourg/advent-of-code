package net.dericbourg.advent

import org.scalatest.{FunSuite, Matchers}

class Day32018Test extends FunSuite with Matchers {

  Seq(
    ("#1323 @ 735,64: 15x18", Request(1323, Coordinates(735, 64), Size(15, 18))),
    ("#1324 @ 153,375: 23x24", Request(1324, Coordinates(153, 375), Size(23, 24))),
    ("#1325 @ 416,214: 21x22", Request(1325, Coordinates(416, 214), Size(21, 22))),
    ("#1326 @ 17,122: 17x24", Request(1326, Coordinates(17, 122), Size(17, 24)))
  ).foreach { case (input, expected) =>
    test(s"Request should parse input string '$input'") {
      Request.parse(input) shouldBe Some(expected)
    }
  }

  test("asRequestRange should convert a request into a Range") {
    val request = Request(123, Coordinates(10,20), Size(2,3))

    val requestRange = Solver.asRequestRange(request)

    requestRange shouldBe RequestRange(
      request.id,
      Set(
        Coordinates(10,20),
        Coordinates(11,20),
        Coordinates(10,21),
        Coordinates(11,21),
        Coordinates(10,22),
        Coordinates(11,22)
      )
    )
  }

}
