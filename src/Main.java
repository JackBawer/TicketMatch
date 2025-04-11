import service.UtilisateurService;

public class Main {
    public static void main(String[] args) {
        try {
            // Use service instead of DAO directly
            UtilisateurService service = new UtilisateurService();

            // Clean up any existing test user first
            cleanupTestUser("jane.doe@example.com");

            // First registration attempt
            boolean result = service.registerUser(
                    "Jane Doe",
                    "jane.doe@example.com",
                    "password123",
                    "client"
            );
            System.out.println("Registration result: " + result);

        } catch (Exception e) {
            System.err.println("Main exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void cleanupTestUser(String email) {
        // Use the UtilisateurService or UtilisateurDAOImpl to delete the user
        // Implementation depends on your available methods
    }
}