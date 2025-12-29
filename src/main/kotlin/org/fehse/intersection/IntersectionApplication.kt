package org.fehse.intersection

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class IntersectionApplication

fun main(args: Array<String>) {
    runApplication<IntersectionApplication>(*args)
}
