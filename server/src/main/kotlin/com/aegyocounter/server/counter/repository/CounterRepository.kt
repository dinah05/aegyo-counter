package com.aegyocounter.server.counter.repository

import com.aegyocounter.server.counter.entity.Counter
import org.springframework.data.jpa.repository.JpaRepository

interface CounterRepository : JpaRepository<Counter, Long>
