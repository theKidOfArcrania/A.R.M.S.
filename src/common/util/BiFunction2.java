package common.util;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface BiFunction2<T, U, R> extends BiFunction<T, U, R> {
	@Override
	default <V> BiFunction2<T, U, V> andThen(Function<? super R, ? extends V> after) {
		Objects.requireNonNull(after);
		return (T t, U u) -> after.apply(apply(t, u));
	}
}
