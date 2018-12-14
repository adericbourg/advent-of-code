name := """advent-of-code-2018-6"""
description := """Advent of code 2018-6"""
organization := "net.dericbourg.advent"

scalaVersion := "2.12.7"
scalacOptions += "-Ypartial-unification"

Testing.dependencies

mainClass in (Compile, run) := Some("net.dericbourg.advent.Run")
