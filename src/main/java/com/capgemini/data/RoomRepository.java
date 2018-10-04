package com.capgemini.data;

import com.capgemini.domain.Room;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class RoomRepository {

    private List<Room> rooms;

    public RoomRepository() {
        rooms = new ArrayList<>();
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public void addRoom(Room room){
        List<Room> allRooms = getAllRooms();
        int id;
        int max = 0;
        for (Room roomIndex : allRooms) {
            id = roomIndex.getRoomID();
            if (id > max) {
                max = id;
            }
        }
        room.setRoomID((short)(max + 1));
        rooms.add(room);
    }

    //Get a single room by roomnumber
    public Room getRoomByRoomNumber(short roomID) {
        List<Room> allRooms = getAllRooms();
        for (Room room : allRooms) {
            if (room.getRoomID() == roomID)
                return room;
        }
        return null;
    }

    public void updateRoom(short id, Room room) {
        Room toReplace = getRoomByRoomNumber(id);
        int index = rooms.indexOf(toReplace);
        rooms.set(index, room);
    }

    public void deleteRoom(Room room) {
        rooms.remove(room);
    }
}