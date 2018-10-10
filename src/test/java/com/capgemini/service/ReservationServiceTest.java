package com.capgemini.service;

import com.capgemini.TestJpaConfig;
import com.capgemini.data.ReservationRepository;
import com.capgemini.domain.Guest;
import com.capgemini.domain.Reservation;
import com.capgemini.domain.Room;
import com.capgemini.domain.RoomType;
import com.capgemini.service.ReservationService;
import com.capgemini.web.MolvenolakeresortApplication;
import org.junit.Assert;
import org.junit.Before;
import com.capgemini.data.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.io.InvalidObjectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationServiceTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationService reservationService;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private Room room1 = new Room();

    private RoomType roomType1;

    @Before
    public void setupRoom() {
        roomType1 = new RoomType((byte) 0, (byte) 2);
        room1.setRoomType(roomType1);

        roomRepository.save(room1);
    }

    @Before
    public void setupReservation() {
        Date startDateReservation1 = null;
        Date endDateReservation1 = null;

        try {
            startDateReservation1 = dateFormat.parse("19-04-2018");
            endDateReservation1 = dateFormat.parse("25-04-2018");
            Guest guest = new Guest();

            Reservation reservation = new Reservation(startDateReservation1, endDateReservation1, guest, 6, room1, roomType1);

            reservationRepository.save(reservation);
        }
         catch(ParseException e){
                e.printStackTrace();
            }
        }

        @Test
        public void getAvailableRoomsNotAvailable () {
            try {
                Date startDateReservation = dateFormat.parse("20-04-2018");
                Date endDateReservation = dateFormat.parse("24-04-2018");
                List<Room> result = reservationService.getAllAvailableRooms(startDateReservation, endDateReservation);

                //comparison, expected = 0 rooms left. compare with size of results
                Assert.assertSame(0, result.size());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void getAvailableRoomsAvailable () {
            try {

                Date startDateReservation = dateFormat.parse("16-04-2018");
                Date endDateReservation = dateFormat.parse("18-04-2018");
                List<Room> result = reservationService.getAllAvailableRooms(startDateReservation, endDateReservation);

                //comparison, expected = 2 rooms left. compare with size of results
                Assert.assertSame(1, result.size());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void getAvailableRoomsNotAvailableEndDateInside () {
            try {

                Date startDateReservation = dateFormat.parse("18-04-2018");
                Date endDateReservation = dateFormat.parse("23-04-2018");
                reservationService.setRoomRepository(roomRepository);
                List<Room> result = reservationService.getAllAvailableRooms(startDateReservation, endDateReservation);

                //comparison, expected = 0 rooms left. compare with size of results
                Assert.assertSame(0, result.size());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void getAvailableRoomsNotAvailableStartDateInside () {
            try {

                Date startDateReservation = dateFormat.parse("23-04-2018");
                Date endDateReservation = dateFormat.parse("28-04-2018");
                List<Room> result = reservationService.getAllAvailableRooms(startDateReservation, endDateReservation);

                //comparison, expected = 0 rooms left. compare with size of results
                Assert.assertSame(0, result.size());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void getAvailableRoomsWithRoomType () {
            try {

                Date startDateReservation = dateFormat.parse("16-04-2018");
                Date endDateReservation = dateFormat.parse("18-04-2018");

                RoomType roomTypeTest = new RoomType((byte)0, (byte)2);
                List<Room> result = reservationService.getAllAvailableRooms(startDateReservation, endDateReservation, roomTypeTest);

                //comparison, expected = 1 rooms left. compare with size of results
                Assert.assertSame(1, result.size());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void getAvailableRoomsWithWrongRoomType () {
            try {

                Date startDateReservation = dateFormat.parse("16-04-2018");
                Date endDateReservation = dateFormat.parse("18-04-2018");

                RoomType roomTypeTest = new RoomType((byte)0, (byte)3);
                reservationService.setRoomRepository(roomRepository);

                List<Room> result = reservationService.getAllAvailableRooms(startDateReservation, endDateReservation, roomTypeTest);

                //comparison, expected = 0 rooms left. compare with size of results
                Assert.assertSame(0, result.size());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }



    @Test
    public void softDeleteReservation() throws InvalidObjectException {
        Reservation reservation = reservationRepository.findAll().stream().findFirst().get();
        reservationService.softDelete(reservation);
        Assert.assertSame(true, reservation.isDeleted());
    }
}

