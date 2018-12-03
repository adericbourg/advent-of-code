name := """advent-of-code-2018"""
description := """Advent of code 2018"""
organization := "net.dericbourg.advent"

lazy val advent2018 = (project in file("."))
    .aggregate(advent20183)

lazy val advent20183 = project in file("3")
