name := """advent-of-code-2018"""
description := """Advent of code 2018"""
organization := "net.dericbourg.advent"

lazy val advent2018 = (project in file("."))
    .aggregate(advent20181)
    .aggregate(advent20183)
    .aggregate(advent20184)

lazy val advent20181 = project in file("1")
lazy val advent20183 = project in file("3")
lazy val advent20184 = project in file("4")
