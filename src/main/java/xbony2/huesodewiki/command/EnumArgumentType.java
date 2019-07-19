package xbony2.huesodewiki.command;

import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.ImmutableMap;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.TranslationTextComponent;

public class EnumArgumentType<T extends Enum<T>> implements ArgumentType<T> {
	private static final DynamicCommandExceptionType INVALID_ENUM = new DynamicCommandExceptionType(obj -> new TranslationTextComponent("commands.argument.enum.invalid", obj));

	private final Class<T> enumClass;
	private final ImmutableMap<String, T> nameToEnum;

	public EnumArgumentType(Class<T> enumClass){
		ImmutableMap.Builder<String, T> builder = ImmutableMap.builder();
		for(T t : enumClass.getEnumConstants()) 
			builder.put(t.name().toLowerCase(Locale.ROOT), t);

		this.enumClass = enumClass;
		this.nameToEnum = builder.build();
	}

	public T getValue(CommandContext<CommandSource> context, String name){
		return context.getArgument(name, enumClass);
	}

	@Override
	public T parse(StringReader reader) throws CommandSyntaxException{
		String key = reader.readUnquotedString();
		T obj = nameToEnum.get(key);

		if(obj == null) 
			throw INVALID_ENUM.createWithContext(reader, key);
		return obj;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder){
		return ISuggestionProvider.suggest(nameToEnum.keySet(), builder);
	}

	@Override
	public Collection<String> getExamples(){
		return nameToEnum.keySet();
	}
}
