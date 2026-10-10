package id.manajemenasrama.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonUtil {
    public static <T> Set<T> toSet(Collection<T> list) {
        return list == null ? Set.of() : list.stream().collect(Collectors.toSet());
    }

    public static <T> List<T> toList(Set<T> set) {
        return set == null ? new ArrayList<>() : new ArrayList<>(set);
    }
}
