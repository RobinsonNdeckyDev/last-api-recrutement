package com.example.api_recrutement.models;

    import lombok.Getter;
    import lombok.Setter;

    public class AuthResponse {
        private String token;
        private UserInfo infosUser;

        public AuthResponse(String token, UserInfo infosUser) {
            this.token = token;
            this.infosUser = infosUser;
        }

        public AuthResponse(String token, String prenom, String nom, String email, String role, String telephone, String description, String adresse, String photoProfil) {
            this.token = token;
            this.infosUser = new UserInfo(prenom, nom, email, role, telephone, description, adresse, photoProfil);
        }

        // Getters and Setters
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserInfo getInfosUser() {
            return infosUser;
        }

        public void setInfosUser(UserInfo infosUser) {
            this.infosUser = infosUser;
        }

        // Classe interne pour stocker les informations utilisateur
        public static class UserInfo {
            @Setter @Getter private String prenom;
            @Setter @Getter private String nom;
            @Setter @Getter private String email;
            @Setter @Getter private String role;
            @Setter @Getter private String telephone;
            @Setter @Getter private String adresse;
            @Setter @Getter private String photoProfil;
            @Setter @Getter private String description;

            public UserInfo(String prenom, String nom, String email, String role, String telephone, String description, String adresse, String photoProfil) {
                this.prenom = prenom;
                this.nom = nom;
                this.email = email;
                this.role = role;
                this.telephone = telephone;
                this.description = description;
                this.adresse = adresse;
                this.photoProfil = photoProfil;
            }
        }
    }