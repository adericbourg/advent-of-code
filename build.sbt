name := """advent-of-code"""
description := """Advent of code"""
organization := "net.dericbourg.advent"

lazy val root = (project in file("."))
    .aggregate(advent2018)

lazy val advent2018 = project in file("2018")
