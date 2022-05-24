# VSC/Local-Testing Branch
Hello! this is where we hold all files that have the final versions of our java server and client

please download [java](https://www.java.com/) to run the .jar files

## Deployment
To deploy this into the IDE of your choosing: 
 1. Type the following command into the terminal to clone our repo:
    ```
    git clone https://github.com/Capstone499/Back-end.git
    ```
 2. Proceed to switch over to the `vsc/local-testing` branch

## Running Code
  1. Start the Server first by writing the following code in the terminal:
      ```
      java -jar server.jar
      ```
  2. Next boot up the client by typing this command into a **separate** terminal:
      ```
      java -jar client.jar
      ```
  3. Afterwards the server terminal should state that a client connected!
  4. On the client terminal it will proceed to ask you to log in.

### Useful things to know
  1. The default credentials are: 
     - username: `user` 
     - password: `pass`
  2. To swap encryptions user must first turn off encryption via `ec off` and then turn on their desired encryption.
  3. Exiting the client is as simple as `exit` after logging in successfully
  4. Exiting the server requires a `ctrl+c` or `command+c` due to the server running on an indefinitely
      - this is by design
  5. The server supports multiple clients!
      - this can easily be tested by opening up another terminal and typing `java -jar client.jar`
