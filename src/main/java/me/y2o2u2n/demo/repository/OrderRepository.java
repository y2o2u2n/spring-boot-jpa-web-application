package me.y2o2u2n.demo.repository;

import me.y2o2u2n.demo.domain.Member;
import me.y2o2u2n.demo.domain.Order;
import me.y2o2u2n.demo.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(me.y2o2u2n.demo.domain.Order order) {
        em.persist(order);
    }

    public me.y2o2u2n.demo.domain.Order findOne(Long id) {
        return em.find(me.y2o2u2n.demo.domain.Order.class, id);
    }

    public List<me.y2o2u2n.demo.domain.Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<me.y2o2u2n.demo.domain.Order> cq = cb.createQuery(me.y2o2u2n.demo.domain.Order.class);
        Root<me.y2o2u2n.demo.domain.Order> o = cq.from(me.y2o2u2n.demo.domain.Order.class);
        Join<me.y2o2u2n.demo.domain.Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }
}
