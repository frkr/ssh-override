only this.

  * channel 3: open failed: connect failed: Connection refused
  * SSH_TUNNEL_ERROR_FORWARD_DISALLOWED
  * ssh tunnel permit error

1) Usage:

`java -jar sshtunnel.jar 23 RemoteHost Port Username Password`

----
2) Inside the Java SSH Console type:

`telnet localhost <port>`

**OR** you may need use another program or configure the Telnet

`
telnet
toggle binary
set echo off
open localhost <port>
`

**OR** Using Perl to connect (_change the `<port>` value_)

`
perl -e 'use IO::Socket;$remote = IO::Socket::INET->new(Proto => "tcp",PeerAddr=> "localhost",PeerPort=> "<port>") or die "Err";while (my $in = <STDIN>) { print $remote $in;my $data;$remote->recv($data, 9999999999);print STDOUT $data;}'
`

**OR** Using Netcat

`
nc localhost <port>
`

----
3) Outside or another terminal just connect to localport.

`
telnet localhost 23
`

**OR** Connect using another program

4) You need to input some chars on Java SSH Console to release the Input ;D

*must be working*
