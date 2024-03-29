package sessionRun;

import server.Server;
import service.DatabaseOperationsService;
import dataAccess.DataAccessException;

class ClientOperationTest {
    private static final Server server = new Server();
    private static final DatabaseOperationsService dbOpsService = new DatabaseOperationsService();
    private static final ConsoleRunner client = new ConsoleRunner();

    private static void setUp() { server.run(3676); }

    public static void tearDown() { server.stop(); }

    public static void main(String [] args) throws DataAccessException {
        setUp();
        clearDatabase();
        client.run();
        clearDatabase();
        tearDown();
    }

    public static void clearDatabase() throws DataAccessException { dbOpsService.clear(); }
}