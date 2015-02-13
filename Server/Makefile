.SUFFIXES: .erl .beam

.erl.beam:
	erlc -W $<

ERL = erl -boot start_clean

MODS = server mochijson2 user

all: compile
	${ERL} -pa '/home/findstr/code/gobang/server' -s server start

compile: ${MODS:%=%.beam}

clean:
	rm -r server.beam mochijson2.beam user.beam
