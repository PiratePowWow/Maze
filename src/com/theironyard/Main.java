package com.theironyard;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

    static final int SIZE = 20;

    static ArrayList<ArrayList<Room>> createRooms(){
        ArrayList<ArrayList<Room>> rooms = new ArrayList<>();
        for (int row = 0; row < SIZE; row ++){
            ArrayList<Room> roomRow = new ArrayList<>();
            for (int col = 0; col < SIZE; col++){
                roomRow.add(new Room(row, col));
            }
            rooms.add(roomRow);
        }
        return rooms;
    }

    static ArrayList<Room> possibleNeighbors(ArrayList<ArrayList<Room>> rooms, int row, int col){
        ArrayList<Room> neighbors  = new ArrayList<>();
        try {
            neighbors.add(rooms.get(row - 1).get(col));
        }catch (Exception e){

        }
        try {
            neighbors.add(rooms.get(row + 1).get(col));
        }catch (Exception e){}
        try{
            neighbors.add((rooms.get(row).get(col - 1)));
        }catch (Exception e){}
        try{
            neighbors.add((rooms.get(row).get(col +1)));
        }catch (Exception e){}

        neighbors = neighbors.stream()
                .filter(room -> {
                    return !room.wasVisited;
                })
                .collect(Collectors.toCollection(ArrayList<Room>::new));
        return neighbors;
    }

    static Room randomNeighbor(ArrayList<ArrayList<Room>> rooms, int row, int col){
        ArrayList<Room> neighbors = possibleNeighbors(rooms, row, col);
        if (neighbors.size() > 0){
            Random r = new Random();
            int index = r.nextInt(neighbors.size());
            return neighbors.get(index);
        }
        return null;
    }

    static void tearDownWall(Room oldRoom, Room newRoom){
        //going up
        if (newRoom.row < oldRoom.row){
            newRoom.hasBottom = false;
        }
        //going down
        else if (newRoom.row > oldRoom.row){
            oldRoom.hasBottom = false;
        }
        //going left
        else if (newRoom.col < oldRoom.col){
            newRoom.hasRight = false;
        }
        //going right
        else if (newRoom.col > oldRoom.col){
            oldRoom.hasRight = false;
        }
    }

    static Room lastOne(ArrayList<ArrayList<Room>> rooms){
        ArrayList<Room> filteredRooms = new ArrayList<>();
        for (ArrayList<Room> row : rooms){
            for (Room room : row){
                if(!room.wasVisited){
                    filteredRooms.add(room);
                }
            }
        }
        if (filteredRooms.size() == 1){
            return filteredRooms.get(0);
        }
        return null;
    }


    static boolean createMaze(ArrayList<ArrayList<Room>> rooms, Room room) {

        room.wasVisited = true;
        Room nextRoom = randomNeighbor(rooms, room.row, room.col);
        Room lastRoom = lastOne(rooms);
        if(lastRoom != null){
            rooms.get(lastRoom.row).get(lastRoom.col).end = true;
        }
        if (nextRoom == null) {
            return false;
        }
        tearDownWall(room, nextRoom);

        while (createMaze(rooms, nextRoom)){
        }
        return true;
    }

    public static void main(String[] args) {
        ArrayList<ArrayList<Room>> rooms = createRooms();
        createMaze(rooms, rooms.get(0).get(0));
        rooms.get(0).get(0).start = true;

        for (ArrayList<Room> row: rooms){
            System.out.print(" _");
        }
        System.out.println();
        for (ArrayList<Room> row : rooms){
            System.out.print("|");
            for ( Room room : row) {
                if(room.start) {
                    System.out.print("O");
                }else if (room.end) {
                    System.out.print("X");
                }else if (room.hasBottom) {
                    System.out.print("_");
                }
                else{
                    System.out.print(" ");
                }
                if (room.hasRight){
                    System.out.print("|");
                }else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
