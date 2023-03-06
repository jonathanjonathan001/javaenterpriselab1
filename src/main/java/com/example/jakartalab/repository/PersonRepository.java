package com.example.jakartalab.repository;

import com.example.jakartalab.entity.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class PersonRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Person> findAll()  {
        Query query = entityManager.createQuery("SELECT p from Person p");
        return (List<Person>) query.getResultList();
    }

    public Optional<Person> findOne(Long id) {
        return Optional.ofNullable(entityManager.find(Person.class, id));
    }

    public void insertPerson(Person person) { entityManager.persist(person); }

    public void deletePerson(Long id) {
        Optional<Person> person = findOne(id);
        person.ifPresent( (p) -> entityManager.remove(p));
    }

    public List<Person> findAllByName(String name) {
        Query query = entityManager.createQuery("SELECT p FROM Person p WHERE p.name LIKE :name");
        query.setParameter("name", name);
        return (List<Person>) query.getResultList();
    }

    public void updatePerson(Person personUpdate) {
        Query query = entityManager.createQuery("UPDATE Person p SET p.name = :name WHERE p.id = :id");
        query.setParameter("id", personUpdate.getId());
        query.setParameter("name", personUpdate.getName());
        query.executeUpdate();
        Query query2 = entityManager.createQuery("UPDATE Person p SET p.cellNumber = :cellNo WHERE p.id = :id");
        query2.setParameter("id", personUpdate.getId());
        query2.setParameter("cellNo", personUpdate.getCellNumber());
        query2.executeUpdate();
    }
}
