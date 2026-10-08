CC = gcc

SRCS = main.c queue.c process.c memory_management.c lru_simulator.c fifo_simulator.c auxiliary.c
OBJS = $(SRCS:.c=.o)

TARGET = main

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) -o $(TARGET) $(OBJS)

%.o: %.c
	$(CC) -c $< -o $@

run: $(TARGET)
	./$(TARGET) $(ARG)

clean:
	rm -f $(OBJS) $(TARGET)