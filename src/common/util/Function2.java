package common.util;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function2<T, R> extends Function<T, R> {
	@Override
	default <V> Function2<T, V> andThen(Function<? super R, ? extends V> after) {
		Objects.requireNonNull(after);
		return (T t) -> after.apply(apply(t));
	}

	default <V, X> BiFunction<T, V, X> filterReturn(BiFunction<R, V, X> after) {
		Objects.requireNonNull(after);
		return (T t, V v) -> after.apply(apply(t), v);
	}
}
