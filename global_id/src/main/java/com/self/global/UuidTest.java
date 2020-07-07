package com.self.global;

import java.util.UUID;

public class UuidTest {
    public static void main(String[] args) {
//        String uuid = UUID.randomUUID().toString();
//        System.out.println(uuid);

        IdWorker idWorker = new IdWorker(1L, 1L, 1L);
        long l = idWorker.nextId();
        System.out.println(l);
    }
}
