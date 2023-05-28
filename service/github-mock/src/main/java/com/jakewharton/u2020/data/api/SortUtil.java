package com.jakewharton.u2020.data.api;

import android.support.annotation.NonNull;
import com.jakewharton.u2020.data.api.model.Repository;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static com.jakewharton.u2020.data.api.Order.ASC;
import static com.jakewharton.u2020.data.api.Order.DESC;

final class SortUtil {

    private static final StarsComparator STARS_ASC = new StarsComparator(ASC);

    private static final StarsComparator STARS_DESC = new StarsComparator(DESC);

    private static final ForksComparator FORKS_ASC = new ForksComparator(ASC);

    private static final ForksComparator FORKS_DESC = new ForksComparator(DESC);

    private static final UpdatedComparator UPDATED_ASC = new UpdatedComparator(ASC);

    private static final UpdatedComparator UPDATED_DESC = new UpdatedComparator(DESC);

    static void sort(List<Repository> repositories, Sort sort, Order order) {
        if (repositories == null)
            return;
        switch(sort) {
            case STARS:
                Collections.sort(repositories, order == ASC ? STARS_ASC : STARS_DESC);
                break;
            case FORKS:
                Collections.sort(repositories, order == ASC ? FORKS_ASC : FORKS_DESC);
                break;
            case UPDATED:
                Collections.sort(repositories, order == ASC ? UPDATED_ASC : UPDATED_DESC);
                break;
        }
    }

    private static abstract class OrderComparator<T> implements Comparator<T> {

        private final Order order;

        protected OrderComparator(Order order) {
            this.order = order;
        }

        @Override
        public final int compare(@NonNull T lhs, @NonNull T rhs) {
            return order == ASC ? compareAsc(lhs, rhs) : -compareAsc(lhs, rhs);
        }

        protected abstract int compareAsc(@NonNull T lhs, @NonNull T rhs);
    }

    private static final class StarsComparator extends OrderComparator<Repository> {

        protected StarsComparator(Order order) {
            super(order);
        }

        @Override
        public int compareAsc(@NonNull Repository lhs, @NonNull Repository rhs) {
            long left = lhs.watchers;
            long right = rhs.watchers;
            return left < right ? -1 : (left == right ? 0 : 1);
        }
    }

    private static final class ForksComparator extends OrderComparator<Repository> {

        protected ForksComparator(Order order) {
            super(order);
        }

        @Override
        public int compareAsc(@NonNull Repository lhs, @NonNull Repository rhs) {
            long left = lhs.forks;
            long right = rhs.forks;
            return left < right ? -1 : (left == right ? 0 : 1);
        }
    }

    private static final class UpdatedComparator extends OrderComparator<Repository> {

        protected UpdatedComparator(Order order) {
            super(order);
        }

        @Override
        public int compareAsc(@NonNull Repository lhs, @NonNull Repository rhs) {
            return lhs.updated_at.compareTo(rhs.updated_at);
        }
    }

    private SortUtil() {
    }
}
