-module(user).
-compile(export_all).

register(L) ->
        io:format("user:register:~p~n", [L]),
        [{<<"status">>, <<"OK">>}, {<<"uid">>, 3}].
