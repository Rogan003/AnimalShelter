package domain.serializeddata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import domain.model.Breed;

import java.util.ArrayList;

@XStreamAlias("breedList")
public class BreedList {
    public static BreedList instance = null;

    @XStreamAlias("breeds")
    private ArrayList<Breed> breeds;

    public BreedList() {
        this.breeds = new ArrayList<Breed>();
    }

    public static BreedList getInstance() {
        if (instance == null) {
            instance = new BreedList();
        }

        return instance;
    }

    public Integer generateId() {
        int amountOfBreeds = breeds.size();
        return amountOfBreeds + 1;
    }

    public Breed addBreed(String name, int speciesId){
        Breed b = new Breed(generateId(), name, speciesId,null);
        this.breeds.add(b);
        return b;
    }

    public static void setInstance(BreedList breeds) {
        instance = breeds;
    }

    public ArrayList<Breed> getBreeds() {
        return this.breeds;
    }

    public void setBreeds(ArrayList<Breed> breeds) {
        this.breeds = breeds;
    }

    public Breed addBreed(Breed breed) {
        this.breeds.add(breed);
        return breed;
    }

    public Breed getBreedByAnimalId(int animalId) {
        for (Breed breed : breeds) {
            if(breed.getAnimals().contains(animalId)) {
                return breed;
            }
        }

        return null;
    }

    public Breed getById(int id){
        for (Breed breed : breeds) {
            if (breed.getId().equals(id)){
                return breed;
            }
        }
        return null;
    }
}
