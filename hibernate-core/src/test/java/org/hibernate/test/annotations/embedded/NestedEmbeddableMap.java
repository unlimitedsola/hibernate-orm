package org.hibernate.test.annotations.embedded;

import org.hibernate.jpa.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.testing.TestForIssue;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;

/**
 * @author Sola
 */
@TestForIssue(jiraKey = "HHH-12985")
public class NestedEmbeddableMap extends BaseEntityManagerFunctionalTestCase {

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[]{A.class};
    }

    @Before
    public void setUp() {
        doInJPA(this::entityManagerFactory, entityManager -> {
            HashMap<Integer, Embedded1> map = new HashMap<>();
            map.put(12, new Embedded1("test", new Embedded2("a", "b")));
            entityManager.persist(new A(1, map));
        });
    }

    @Test
    public void test() {
        // won't able to build SessionFactory
    }

    @Entity
    public static class A {
        @Id
        private final Integer id;
        @ElementCollection
        private final Map<Integer, Embedded1> embedded1Map;

        public A(Integer id, Map<Integer, Embedded1> embedded1Map) {
            this.id = id;
            this.embedded1Map = embedded1Map;
        }

        public Integer getId() {
            return id;
        }

        public Map<Integer, Embedded1> getEmbedded1Map() {
            return embedded1Map;
        }
    }

    @Embeddable
    public static class Embedded1 {
        private final String name;
        private final Embedded2 embedded2;

        public Embedded1(String name, Embedded2 embedded2) {
            this.name = name;
            this.embedded2 = embedded2;
        }

        public String getName() {
            return name;
        }

        public Embedded2 getEmbedded2() {
            return embedded2;
        }
    }

    @Embeddable
    public static class Embedded2 {
        private final String a;
        private final String b;

        public Embedded2(String a, String b) {
            this.a = a;
            this.b = b;
        }

        public String getA() {
            return a;
        }

        public String getB() {
            return b;
        }
    }

}
