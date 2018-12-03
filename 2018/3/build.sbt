name := """advent-of-code-2018-3"""
description := """Advent of code 2018-3"""
organization := "net.dericbourg.advent"

scalaVersion := "2.12.7"
scalacOptions += "-Ypartial-unification"

Testing.dependencies

mainClass in (Compile, run) := Some("net.dericbourg.advent.Day32018")
