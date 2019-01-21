# Client_android
Robot 클래스를 이용하여 PC측 마우스 제어를 위한 클라이언트 측 안드로이드 어플리케이션.

# Server_PC
클라이언트 측에서 보내는 데이터를 Socket으로 받기위한 server 측 자바 어플리케이션.


개요
-----------------------
Client 측에서 Server 쪽으로 ip,port 를 이용하여 소켓통신을 기반으로 하였습니다.
Server 측에서는 Client 쪽에서 보낸 데이터를 받아 Server 측의 Robot 클래스를 이용하여 간단한 마우스 제어를 하였습니다.

Client 측에서는 Action 버튼을 누를때 마다 x,y 좌표값 0,0부터 각각 20,30 씩 증가하는 값을 보내도록 하였습니다.
Server 측에서는 이 값을 받아 Robot 클래스의 mouseMove 메소드를 사용하여 마우스 포인터를 움직여 보았습니다.


Screenshots
-----------------------
<div>
<img width="300" src = "https://user-images.githubusercontent.com/31503178/51472295-66f68580-1dbc-11e9-8d61-500a07c570a1.png">
<img width="300" src = https://user-images.githubusercontent.com/31503178/51472175-1ed76300-1dbc-11e9-84f1-a774790ab14e.jpg>
</div>
