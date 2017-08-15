package edu.rosehulman.lix4.petlf;

/**
 * Created by phillee on 7/21/2017.
 */

public class Util {

    public static String[] getCatBreeds() {
        String[] breeds = new String[]{
                "Abyssinian",
                "American Bobtail",
                "American Curl",
                "American Shorthair",
                "American Wirehair",
                "Balinese",
                "Calico",
                "Bengal",
                "Birman",
                "Bombay",
                "British Shorthair",
                "Burmese",
                "Chartreux",
                "Chausie",
                "Cornish Rex",
                "Devon Rex",
                "Donskoy",
                "Egyptian Mau",
                "Exotic Shorthair",
                "Havana",
                "Highlander",
                "Himalayan",
                "Household Pet Cat",
                "Household Pet Kitten",
                "Japanese Bobtail",
                "Korat",
                "Kurilian Bobtail",
                "LaPerm",
                "Maine Coon",
                "Manx",
                "Minskin",
                "Munchkin",
                "Nebelung",
                "Norwegian Forest Cat",
                "Ocicat",
                "Ojos Azules",
                "Oriental",
                "Persian",
                "Peterbald",
                "Pixiebob",
                "Ragdoll",
                "Russian Blue",
                "Savannah",
                "Scottish Fold",
                "Selkirk Rex",
                "Serengeti",
                "Siberian",
                "Siamese",
                "Singapura",
                "Snowshoe",
                "Sokoke",
                "Somali",
                "Sphynx",
                "Thai",
                "Tonkinese",
                "Toyger",
                "Turkish Angora",
                "Turkish Van"
        };
        return breeds;
    }

    public static String[] getSize() {
        return new String[]{"Big", "Medium", "Small"};
    }

    public static String[] getDogBreeds() {

        return new String[]{
                "Affenpinscher",
                "Afghan Hound",
                "Airedale Terrier",
                "Akita",
                "Alaskan Malamute",
                "Standard American Eskimo Dog"
        };
    }
}
