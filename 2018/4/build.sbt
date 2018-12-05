name := """advent-of-code-2018-4"""
description := """Advent of code 2018-4"""
organization := "net.dericbourg.advent"

scalaVersion := "2.12.7"
scalacOptions += "-Ypartial-unification"

Testing.dependencies

mainClass in (Compile, run) := Some("net.dericbourg.advent.Day42018")
