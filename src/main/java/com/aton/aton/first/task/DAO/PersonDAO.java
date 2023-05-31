package com.aton.aton.first.task.DAO;

import com.aton.aton.first.task.model.Person;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PersonDAO {

    private long indexOfNextAccount = 0L;
    private final Map<Long, Person> mapOnKeyAccount;
    private final Map<String, List<Person>> mapOnKeyName;
    private final Map<Double, List<Person>> mapOnKeyValue;

    {
        Person person = new Person(indexOfNextAccount++, "Anton", 123.456);
        Person person1 = new Person(indexOfNextAccount++, "Anton", 54.3);
        Person person2 = new Person(indexOfNextAccount++, "Anton", 666.66);
        mapOnKeyAccount = new HashMap<>();
        mapOnKeyName = new HashMap<>();
        mapOnKeyValue = new HashMap<>();

        mapOnKeyAccount.put(person.getAccount(), person);
        mapOnKeyAccount.put(person1.getAccount(), person1);
        mapOnKeyAccount.put(person2.getAccount(), person2);

        mapOnKeyName.putIfAbsent(person.getName(), new ArrayList<>());
        mapOnKeyName.putIfAbsent(person1.getName(), new ArrayList<>());
        mapOnKeyName.putIfAbsent(person2.getName(), new ArrayList<>());
        mapOnKeyName.get(person.getName()).add(person);
        mapOnKeyName.get(person1.getName()).add(person1);
        mapOnKeyName.get(person2.getName()).add(person2);

        mapOnKeyValue.putIfAbsent(person.getValue(), new ArrayList<>());
        mapOnKeyValue.putIfAbsent(person1.getValue(), new ArrayList<>());
        mapOnKeyValue.putIfAbsent(person2.getValue(), new ArrayList<>());
        mapOnKeyValue.get(person.getValue()).add(person);
        mapOnKeyValue.get(person1.getValue()).add(person1);
        mapOnKeyValue.get(person2.getValue()).add(person2);
    }

    public Person findPerson(long idAccount) {
        return mapOnKeyAccount.get(idAccount);
    }

    public List<Person> getPersonByAccount(long idAccount) {
        return (mapOnKeyAccount.containsKey(idAccount)) ? List.of(mapOnKeyAccount.get(idAccount)) : null;
    }

    public List<Person> getPersonByName(String name) {
        return mapOnKeyName.get(name);
    }

    public List<Person> getPersonByValue(Double value) {
        return mapOnKeyValue.get(value);
    }

    public void deletePerson(long accountId) {
        Person person = mapOnKeyAccount.get(accountId);
        mapOnKeyAccount.remove(person.getAccount());

        mapOnKeyName.get(person.getName()).remove(person);
        if (mapOnKeyName.get(person.getName()).isEmpty()) mapOnKeyName.remove(person.getName());

        mapOnKeyValue.get(person.getValue()).remove(person);
        if (mapOnKeyValue.get(person.getValue()).isEmpty()) mapOnKeyValue.remove(person.getValue());
    }

    public void updatePerson(long account, Person person) {
        person.setAccount(account);
        deletePerson(account);
        addToMaps(person);
    }

    public void addToMaps(Person person) {
        mapOnKeyAccount.put(person.getAccount(), person);

        mapOnKeyName.putIfAbsent(person.getName(), new ArrayList<>());
        mapOnKeyName.get(person.getName()).add(person);

        mapOnKeyValue.putIfAbsent(person.getValue(), new ArrayList<>());
        mapOnKeyValue.get(person.getValue()).add(person);
    }

    public void save(Person person) {
        if (mapOnKeyAccount.size() == Integer.MAX_VALUE) return; // Максимум записей достигнут
        while (mapOnKeyAccount.containsKey(indexOfNextAccount)) {
            indexOfNextAccount++;
            if (indexOfNextAccount == Integer.MAX_VALUE) indexOfNextAccount = 0L;
        }
        person.setAccount(indexOfNextAccount++);
        addToMaps(person);
    }
}
