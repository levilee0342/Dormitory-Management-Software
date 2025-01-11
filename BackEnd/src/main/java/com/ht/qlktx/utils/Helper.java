package com.ht.qlktx.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.qlktx.entities.*;

import java.io.File;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public class Helper {
    public static String generateRandomSecret(int length) {
        byte[] randomBytes = new byte[length];
        new SecureRandom().nextBytes(randomBytes);

        // Use Base64 encoding to represent the random bytes as a string
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public static List<RoomType> createSeedRoomTypes() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/java/com/ht/qlktx/utils/room_types.json"), new TypeReference<List<RoomType>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Region> createSeedRegions() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/java/com/ht/qlktx/utils/regions.json"), new TypeReference<List<Region>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Account> createSeedAccounts() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/java/com/ht/qlktx/utils/accounts.json"), new TypeReference<List<Account>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Student> createSeedStudents() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/java/com/ht/qlktx/utils/students.json"), new TypeReference<List<Student>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Staff> createSeedStaffs() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/java/com/ht/qlktx/utils/staffs.json"), new TypeReference<List<Staff>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    public static List<BookingTime> createSeedBookingTimes() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/java/com/ht/qlktx/utils/bookingtimes.json"), new TypeReference<List<BookingTime>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Discount> createSeedDiscounts() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/java/com/ht/qlktx/utils/discounts.json"), new TypeReference<List<Discount>>() {});
        } catch (Exception e) {
            return null;
        }
    }
}
