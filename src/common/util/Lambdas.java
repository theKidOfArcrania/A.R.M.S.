package common.util;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * This contains various lambda utility functions.
 */
public class Lambdas {
	public static <T, U, R> BiFunction2<T, U, R> bifunctionAdaptor(BiConsumer<T, U> consumer, R staticReturn) {
		return (t, u) -> {
			consumer.accept(t, u);
			return staticReturn;
		};
	}

	public static <T, U> Function2<T, U> convertType(Class<U> convertTo) {
		return (t) -> convertTo.cast(t);
	}

	public static <V, T, U, Z> BiConsumer<V, U> filterFirstArg(Function<V, Z> filter1, Function<Z, T> filter2, BiConsumer<T, U> c) {
		return (v, u) -> c.accept(filter2.apply(filter1.apply(v)), u);
	}

	private Lambdas() {
	}
}
