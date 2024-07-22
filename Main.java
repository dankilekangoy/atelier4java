import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "contacts.txt";
    private static Map<String, Contact> contacts = new HashMap<>();

    public static void main(String[] args) {
        try {
            loadContacts();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors du chargement des contacts: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        int choix;

        do {
            System.out.println("\nMenu");
            System.out.println("1. Ajouter un contact ");
            System.out.println("2. Supprimer un contact ");
            System.out.println("3. Modifier un contact par identifiant ");
            System.out.println("4. Chercher un contact par Nom ");
            System.out.println("5. Lister les contacts par lettre ");
            System.out.println("6. Afficher le nombre de contacts ");
            System.out.println("7. Afficher le contact par type ");
            System.out.println("8. Afficher le détail d'un contact par son identifiant ");
            System.out.println("9. Quitter");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    ajouterContact(scanner);
                    break;
                case 2:
                    supprimerContact(scanner);
                    break;
                case 3:
                    modifierContact(scanner);
                    break;
                case 4:
                    rechercherContactParNom(scanner);
                    break;
                case 5:
                    listerContactParLettre(scanner);
                    break;
                case 6:
                    afficherNombreDeContacts();
                    break;
                case 7:
                    afficherContactParType(scanner);
                    break;
                case 8:
                    afficherDetailsContactParId(scanner);
                    break;
                case 9:
                    try {
                        saveContacts();
                    } catch (IOException e) {
                        System.out.println("Erreur lors de la sauvegarde des contacts: " + e.getMessage());
                    }
                    System.out.println("Merci d'avoir utilisé notre application.");
                    break;
                default:
                    System.out.println("Option invalide");
            }
        } while (choix != 9);
        scanner.close();
    }

    private static void ajouterContact(Scanner scanner) {
        System.out.println("Entrez l'ID du contact: ");
        String id = scanner.nextLine();
        System.out.println("Entrez le nom du contact: ");
        String name = scanner.nextLine();
        System.out.println("Entrez le numéro du contact: ");
        String phoneNumber = scanner.nextLine();
        System.out.println("Type de contact 1. pour personnel, 2. pour professionnel: ");
        int type = scanner.nextInt();
        scanner.nextLine();

        Contact contact;
        if (type == 1) {
            contact = new ContactPersonnel(id, name, phoneNumber);
        } else {
            contact = new ContactProfessionnel(id, name, phoneNumber);
        }
        contacts.put(id, contact);
        System.out.println("Contact ajouté avec succès");
    }

    private static void supprimerContact(Scanner scanner) {
        System.out.println("Entrez l'ID du contact: ");
        String id = scanner.nextLine();
        if (contacts.remove(id) != null) {
            System.out.println("Contact supprimé avec succès");
        } else {
            System.out.println("Contact non trouvé");
        }
    }

    private static void modifierContact(Scanner scanner) {
        System.out.println("Entrez l'ID du contact à modifier: ");
        String id = scanner.nextLine();
        Contact contact = contacts.get(id);
        if (contact != null) {
            System.out.println("Entrez le nouveau nom du contact: ");
            String name = scanner.nextLine();
            System.out.println("Entrez le nouveau numéro de téléphone: ");
            String phoneNumber = scanner.nextLine();
            contact.setName(name);
            contact.setPhoneNumber(phoneNumber);
            System.out.println("Contact modifié avec succès");
        } else {
            System.out.println("Contact non trouvé");
        }
    }

    private static void rechercherContactParNom(Scanner scanner) {
        System.out.println("Entrez le nom du contact: ");
        String name = scanner.nextLine();
        boolean found = false;
        for (Contact contact : contacts.values()) {
            if (contact.getName().equalsIgnoreCase(name)) {
                System.out.println(contact);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Contact non trouvé");
        }
    }

    private static void listerContactParLettre(Scanner scanner) {
        System.out.println("Entrez la lettre alphabétique: ");
        char lettre = scanner.nextLine().charAt(0);
        boolean found = false;
        for (Contact contact : contacts.values()) {
            if (contact.getName().charAt(0) == lettre) {
                System.out.println(contact);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Aucun contact trouvé avec cette lettre");
        }
    }

    private static void afficherNombreDeContacts() {
        System.out.println("Nombre total de contacts: " + contacts.size());
    }

    private static void afficherContactParType(Scanner scanner) {
        System.out.println("Type de contact à afficher (1. personnel, 2. professionnel): ");
        int type = scanner.nextInt();
        scanner.nextLine();
        for (Contact contact : contacts.values()) {
            if ((type == 1 && contact instanceof ContactPersonnel) || (type == 2 && contact instanceof ContactProfessionnel)) {
                System.out.println(contact);
            }
        }
    }

    private static void afficherDetailsContactParId(Scanner scanner) {
        System.out.println("Entrez l'ID du contact: ");
        String id = scanner.nextLine();
        Contact contact = contacts.get(id);
        if (contact != null) {
            System.out.println(contact);
        } else {
            System.out.println("Contact non trouvé");
        }
    }

    private static void saveContacts() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(contacts);
        }
    }

    private static void loadContacts() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                contacts = (Map<String, Contact>) ois.readObject();
            }
        }
    }
}
