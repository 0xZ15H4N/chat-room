A small multi threaded chat room that works on LAN ! 

  To complile just navigate to ./src
    ` javac Color.java Server.java Client.java `

  and after that first run 
    ` java Server ` 

  and after that you can run Client file one by one to connect 
     ` java Client ` 

  ![image](https://github.com/user-attachments/assets/4a89cb04-68d1-41bf-8675-7726180cb00f)


TO DO :

      1 ) updated the logic so it can connect to a server with different IP on same LAN
                        |
                        |-> for it will send a single on dedicated port on the range of IP from 0-255
                        |
                        |-> if recieve a response from a ip , then it will connect to it.
                  
      
      2 ) update the logic to add an Authentication (password) for cient to enter the chat
      
      
     
FUTURE GOAL :
        
        Creating an java based application using java.awt .      
