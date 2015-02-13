-module(server).
-export([start/0]).

-define(PORT,8989).

%%test main function
start() ->
        case    gen_tcp:listen( ?PORT,[binary,{active,true},{packet,line}]) of
                {ok, LSocket} ->
                        io:format("listen port success ~w~n",[LSocket]),
                        register(gobang, spawn(fun() -> listen(LSocket) end)),{ok,LSocket};
                {error,Reason}->
                        io:format("listen port error Reason: ~w~n",[Reason]),
                        {error,{cannot_listen,Reason}}
        end .
 
listen(LSocket) ->
        io:format("begin listen lsocket: ~n",[]),
        case gen_tcp:accept(LSocket) of
                {ok,Socket}->
                        io:format("receive client connect: ~w~n",[Socket]),
                        spawn(fun()-> listen(LSocket) end), loop(Socket);
                {stop} ->
                        io:format("stopped by user"),
                        gen_tcp:close(LSocket);
                {error,Reason}->
                        io:format("accept client error: ~w~n",[Reason])
        end.

loop(Socket)->
        io:format("receive listen socket client data: ~n",[]),
        receive
                {tcp,Socket,Data}->
                        io:format("receive client data1: ~ts~n",[Data]),
                        {struct, [Cmd]} = mochijson2:decode(Data),
                        handler(Socket, Cmd),
                        loop(Socket);
                Data1 ->
                        io:format("unexpected error~p~n", [Data1])
        end.

handler(Socket, L)->
        case L of
                {<<"register">>, Param}->
                        Status = user:register(Param),
                        Json = mochijson2:encode({struct, Status}),
                        Bin = list_to_binary(Json),
                        Line = <<Bin/bits, "\r\n">>,
                        io:format("handler ~p size:~p ~n", [Line, size(Line)]),
                        gen_tcp:send(Socket, Line);
                Data ->
                        io:format("handler ~p ~n", [Data])
        end.

 
close()->
        gobang ! stop.
