#include <iostream>
#include <fstream>
#include <vector>

class Bus {
 public:
  Bus() = default;

  Bus(int id) : id_(id) {}

  void Iterate() {
    ++counter_;
    if (counter_ == id_)
      counter_ = 0;
  }

  int GetCounter() const {
    return counter_;
  }

  int GetId() const {
    return id_;
  }

 private:
  int id_;
  int
  int counter_ = 0;
};

std::vector<std::string> GetWords(std::string str) {
  std::vector<std::string> words;

  std::string word("");
  for (char c : str) {
    if (c != ',') {
      word += c;
    } else {
      words.emplace_back(word);
      word = "";
    }
  }

  return words;
}

std::pair<long, std::vector<Bus>> ParseInput() {
  std::ifstream input("input.txt");

  long timestamp;
  input >> timestamp;

  std::string raw_bus_ids;
  input >> raw_bus_ids;

  std::vector<std::string> bus_ids = GetWords(raw_bus_ids);

  std::vector<Bus> busses;

  for (int i = 0; i < bus_ids.size(); ++i)
    if (bus_ids[i] != "x")
      busses.emplace_back(std::stoi(bus_ids[i]));

  return {timestamp, busses};
}

void IterateBusses(std::vector<Bus> &busses) {
  for (Bus &bus : busses)
    bus.Iterate();
}

int main() {
  std::vector<Bus> busses;

  std::pair<long, std::vector<Bus>> in = ParseInput();

  busses = in.second;

  long current_timestamp = 0;

//  for (current_timestamp = 0; current_timestamp < starting_timestamp; ++current_timestamp)
//    IterateBusses(busses);
//
//  int delay;
//  Bus earliest_bus;
//  for (delay = 0; delay < 13; ++delay) {
//    for (Bus &bus : busses) {
//      if (bus.GetCounter() == 0) {
//        earliest_bus = bus;
//        goto EXIT;
//      }
//      bus.Iterate();
//    }
//  }
//  EXIT: ;



  return 0;
}
