package dev.abykov.cloudmarketplace.menu.dto;

import dev.abykov.cloudmarketplace.menu.entity.MenuItem;
import dev.abykov.cloudmarketplace.menu.entity.MenuItem_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public enum MenuItemSort {

    PRICE_ASC {
        @Override
        public Order toOrder(CriteriaBuilder cb, Root<MenuItem> root) {
            return cb.asc(root.get(MenuItem_.price));
        }
    },

    PRICE_DESC {
        @Override
        public Order toOrder(CriteriaBuilder cb, Root<MenuItem> root) {
            return cb.desc(root.get(MenuItem_.price));
        }
    },

    NAME_ASC {
        @Override
        public Order toOrder(CriteriaBuilder cb, Root<MenuItem> root) {
            return cb.asc(root.get(MenuItem_.name));
        }
    },

    NAME_DESC {
        @Override
        public Order toOrder(CriteriaBuilder cb, Root<MenuItem> root) {
            return cb.desc(root.get(MenuItem_.name));
        }
    },

    CREATED_AT_ASC {
        @Override
        public Order toOrder(CriteriaBuilder cb, Root<MenuItem> root) {
            return cb.asc(root.get(MenuItem_.createdAt));
        }
    },

    CREATED_AT_DESC {
        @Override
        public Order toOrder(CriteriaBuilder cb, Root<MenuItem> root) {
            return cb.desc(root.get(MenuItem_.createdAt));
        }
    };

    public abstract Order toOrder(CriteriaBuilder cb, Root<MenuItem> root);
}
