package com.ht.qlktx;

import com.ht.qlktx.entities.*;
import com.ht.qlktx.enums.RoomStatus;
import com.ht.qlktx.modules.account.AccountRepository;
import com.ht.qlktx.modules.account.RoleRepository;
import com.ht.qlktx.modules.booking.repositories.BookingTimeRepository;
import com.ht.qlktx.modules.discount.DiscountRepository;
import com.ht.qlktx.modules.region.RegionRepository;
import com.ht.qlktx.modules.room.RoomRepository;
import com.ht.qlktx.modules.room_type.RoomTypeRepository;
import com.ht.qlktx.modules.staff.StaffRepository;
import com.ht.qlktx.modules.student.StudentRepository;
import com.ht.qlktx.utils.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServerCommandLineRunner implements CommandLineRunner {
    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;
    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final BookingTimeRepository bookingTimeRepository;
    private final DiscountRepository discountRepository;

    @Override
    public void run(String... args) {
        List<Student> students = Helper.createSeedStudents();
        List<Staff> staffs = Helper.createSeedStaffs();
        List<Account> accounts = Helper.createSeedAccounts();
        List<RoomType> roomTypes = Helper.createSeedRoomTypes();
        List<Region> regions = Helper.createSeedRegions();
        List<BookingTime> bookingTimes = Helper.createSeedBookingTimes();
        List<Discount> discounts = Helper.createSeedDiscounts();

        List<Region> savedRegions;
        List<RoomType> savedRoomTypes;

        if (roleRepository.count() == 0) {
            List<Role> roles = List.of(
                    Role.builder().role(com.ht.qlktx.enums.Role.STUDENT.toString()).build(),
                    Role.builder().role(com.ht.qlktx.enums.Role.STAFF.toString()).build(),
                    Role.builder().role(com.ht.qlktx.enums.Role.ADMIN.toString()).build()
            );
            roleRepository.saveAll(roles);
        }

        if (regionRepository.count() == 0 && regions != null) {
            savedRegions = regionRepository.saveAll(regions);
        } else {
            savedRegions = null;
        }

        if (roomTypeRepository.count() == 0 && roomTypes != null) {
            savedRoomTypes = roomTypeRepository.saveAll(roomTypes);
        } else {
            savedRoomTypes = null;
        }

        if (savedRegions != null && savedRoomTypes != null) {
            List<Room> generatedRooms = new ArrayList<>();
            savedRegions.forEach(region -> {
                for (int i = 0; i < roomTypes.size() * 2; i++) {
                    String roomId = region.getId() + (i + 1);
                    Room room = Room.builder()
                            .id(roomId)
                            .region(region)
                            .type(roomTypes.get(i % roomTypes.size()))
                            .deleted(false)
                            .status(RoomStatus.AVAILABLE)
                            .build();
                    generatedRooms.add(room);
                }
            });

            roomRepository.saveAll(generatedRooms);
        }

        if (accountRepository.count() == 0 && accounts != null) {
            var student = roleRepository.findByRole(com.ht.qlktx.enums.Role.STUDENT.toString()).orElse(null);
            var staff = roleRepository.findByRole(com.ht.qlktx.enums.Role.STAFF.toString()).orElse(null);
            var admin = roleRepository.findByRole(com.ht.qlktx.enums.Role.ADMIN.toString()).orElse(null);

            if (student == null || staff == null || admin == null) {
                System.out.println("Some roles are missing. Please check the database.");
                return;
            }

            List<Account> transformedAccounts = accounts.stream().peek(account -> {
                account.setPassword(passwordEncoder.encode(account.getUsername()));
                if (account.getUsername().contains("N21")) {
                    account.setRole(student);
                } else {
                    if (account.getUsername().equals("QL0001")) {
                        account.setRole(admin);
                    } else {
                        account.setRole(staff);
                    }
                }
            }).toList();

            accountRepository.saveAll(transformedAccounts);

            if (staffs != null) {
                staffs.forEach(s -> {
                    s.setAccount(accountRepository.findById(s.getId()).orElse(null));
                });
                staffRepository.saveAll(staffs);
            }

            if (students != null) {
                students.forEach(s -> {
                    s.setAccount(accountRepository.findById(s.getId()).orElse(null));
                });
                studentRepository.saveAll(students);
            }
        }

        if (bookingTimeRepository.count() == 0 && bookingTimes != null) {
            bookingTimeRepository.saveAll(bookingTimes);
        }

        if (discountRepository.count() == 0 && discounts != null) {
            discountRepository.saveAll(discounts);
        }
    }
}