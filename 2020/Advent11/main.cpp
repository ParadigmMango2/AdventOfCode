#include <iostream>
#include <array>
#include <string>
#include <fstream>

const std::pair<int, int> moves[8] = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0},
                                      {1, 0}, {-1, 1}, {0, 1}, {1, 1}};

std::array<std::string, 93> ParseInput() {
  std::array<std::string, 93> seats;

  std::ifstream in("input.txt");

  for (int i = 0; i < 93; ++i) {
    std::string row;

    in >> row;

    seats[i] = row;
  }

  return seats;
}

bool SeesOccupiedSeat(const std::pair<int, int> &move, int row, int col, const std::array<std::string, 93> &seats) {
  do {
    row += move.first;
    col += move.second;
  } while (row > -1 && row < 93 && col > -1 && col < 90 && seats[row][col] != '#' && seats[row][col] != 'L');

  if(row < 0 || row > 92 || col < 0 || col > 89 || seats[row][col] == 'L')
    return false;
  else
    return true;
}

int CountVisible(const int &row, const int &col, const std::array<std::string, 93> &seats) {
  int count = 0;

  for (std::pair<int, int> seat_coord : moves)
    if (SeesOccupiedSeat(seat_coord, row, col, seats))
      ++count;

  return count;
}

std::array<std::string, 93> IterateSeats(const std::array<std::string, 93> &seats) {
  std::array<std::string, 93> new_seats;

  for (int row = 0; row < 93; ++row) {
    std::string new_row;

    for (int col = 0; col < 90; ++col) {
      if (seats[row][col] == 'L' && CountVisible(row, col, seats) == 0)
        new_row += '#';
      else if (seats[row][col] == '#' && CountVisible(row, col, seats) >= 5)
        new_row += 'L';
      else
        new_row += seats[row][col];
    }

    new_seats[row] = new_row;
  }

  return new_seats;
}

std::array<std::string, 93> GetStableSeats(std::array<std::string, 93> seats) {
  std::array<std::string, 93> prev_seats;

  while (seats != prev_seats) {
    prev_seats = seats;

    seats = IterateSeats(seats);
  }

  return prev_seats;
}

int CountOccupiedSeats(const std::array<std::string, 93> &seats) {
  int count = 0;

  for (const std::string &row : seats)
    for (const char &seat : row)
      if (seat == '#')
        ++count;

  return count;
}

int main() {
  std::array<std::string, 93> in = ParseInput();

  std::array<std::string, 93> stable = GetStableSeats(in);

  std::cout << SeesOccupiedSeat(moves[6], 0, 0, in);

  std::cout << CountOccupiedSeats(stable);

  return 0;
}
