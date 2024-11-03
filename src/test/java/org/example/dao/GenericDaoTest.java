package org.example.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenericDaoTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    private GenericDao<MockEntity, Long> dao;

    public static class MockEntity {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        dao = Mockito.spy(new GenericDao<>(MockEntity.class, sessionFactory));

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);

        doNothing().when(transaction).commit();
    }

    @Test
    void testSave_Success() {
        MockEntity entity = new MockEntity();
        entity.setName("Test Entity");

        doNothing().when(session).persist(entity);
        dao.save(entity);

        verify(session).persist(entity);
        verify(transaction).commit();
    }

    @Test
    void testFindById_EntityExists() {
        MockEntity entity = new MockEntity();
        entity.setId(1L);

        when(session.get(MockEntity.class, 1L)).thenReturn(entity);

        MockEntity result = dao.findById(1L);

        assertEquals(entity, result);
        verify(session).get(MockEntity.class, 1L);
        verify(transaction).commit();
    }

    @Test
    void testFindById_EntityDoesNotExist() {
        when(session.get(MockEntity.class, 1L)).thenReturn(null);

        MockEntity result = dao.findById(1L);

        assertNull(result);
        verify(session).get(MockEntity.class, 1L);
        verify(transaction).commit();
    }

    @Test
    void testFindAll_ReturnsList() {
        List<MockEntity> expectedList = List.of(new MockEntity(), new MockEntity());

        doReturn(expectedList).when(dao).findAll();

        List<MockEntity> result = dao.findAll();

        assertEquals(expectedList, result);
        verify(dao).findAll();
    }

    @Test
    void testFindWithQuery_ReturnsResults() {
        String hql = "from MockEntity where name = :name";
        Map<String, Object> params = Map.of("name", "test");

        doReturn(List.of(new MockEntity())).when(dao).findWithQuery(eq(hql), eq(MockEntity.class), eq(params));

        List<MockEntity> result = dao.findWithQuery(hql, MockEntity.class, params);

        assertEquals(1, result.size());
        verify(dao).findWithQuery(hql, MockEntity.class, params);
    }

    @Test
    void testUpdate_EntityExists() {
        MockEntity existingEntity = new MockEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Old Name");

        MockEntity updatedEntity = new MockEntity();
        updatedEntity.setName("New Name");

        when(session.get(MockEntity.class, 1L)).thenReturn(existingEntity);

        int result = dao.update(1L, updatedEntity);

        assertEquals(1, result);
        assertEquals("New Name", existingEntity.getName());
        verify(transaction).commit();
    }

    @Test
    void testUpdate_EntityDoesNotExist() {
        MockEntity updatedEntity = new MockEntity();
        updatedEntity.setName("New Name");

        when(session.get(MockEntity.class, 1L)).thenReturn(null);

        int result = dao.update(1L, updatedEntity);

        assertEquals(0, result);
        verify(session).get(MockEntity.class, 1L);
        verify(transaction, never()).commit();
    }

    @Test
    void testDelete_EntityExists() {
        MockEntity entity = new MockEntity();
        entity.setId(1L);

        when(session.get(MockEntity.class, 1L)).thenReturn(entity);

        int result = dao.delete(1L);

        assertEquals(1, result);
        verify(session).remove(entity);
        verify(transaction).commit();
    }
}