package model;

public class Utilisateur {
    private Integer idUtilisateur; // Maps to id_utilisateur
    private String nom;        // Maps to nom
    private String email;      // Maps to email
    private String motDePasse; // Maps to mot_de_passe
    private userRole role;       // Maps to role (client/admin)
    private double balance;

    public enum userRole {
        USER, ADMIN
    }

    // Default constructor
    public Utilisateur() {
    }

    // Constructor without idUtilisateur (for new users)
    public Utilisateur(String nom, String email, String motDePasse, userRole role, int balance) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = userRole.USER;
        this.balance = balance;
    }

    // Constructor with idUtilisateur (for existing users)
    public Utilisateur(Integer idUtilisateur, String nom, String email, String motDePasse, userRole role, int balance) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = userRole.USER;
        this.balance = balance;
    }

    // Getters and Setters
    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public userRole getRole() {
        return role;
    }

    public void setRole(userRole role) {
        this.role = role;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUtilisateur=" + idUtilisateur +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}