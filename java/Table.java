import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;

//represents a table i.e. text channel
public class Table
{
	//TODO: implement editing data
	//TODO: add method to return specific row
	//TODO: add auto increment ability
	
	//TODO: add methods for returning resultset or executing on resultset instead of first found value
	//TODO: add methods for specifying AND or OR in retrievals (.get())
	//TODO: add support for different variable types
	
	private TextChannel textchannel;
	private String[] columns;
	
	//TODO: verify the guild is a database
	public Table(Guild guild, String name, String... columns)
	{
		guild.createTextChannel(name).queue();
	}
	
	public static boolean deleteTable(Table table)
	{
		table.getTextChannel().delete().queue();
		return false; //TODO proper response
	}
	
	public boolean delete()
	{
		textchannel.delete().queue();
		return false; //TODO proper response
	}
	
	public boolean put(String... data)
	{
		if(data.length != columns.length) //TODO: flag as error?
			return false;
		
		textchannel.sendMessage(createDataRow(data)).queue();
		return true;
	}
	
	public static boolean put(Table table, String... data)
	{
		if(data.length != table.getColumns().length) //TODO: flag as error?
			return false;
		
		table.getTextChannel().sendMessage(createDataRow(table, data)).queue();
		return true;
	}
	
	//messageembed emulates a row of data, fields and values represent columns and values of columns
	private MessageEmbed createDataRow(String[] data)
	{
		EmbedBuilder databuilder = new EmbedBuilder()
				.setColor(65280)
				.setTitle("Data Entry:")
				.setTimestamp(Instant.now());
		
		//should already have verified that columns array is the same length as data array
		for(int i = 0; i < data.length; i++)
			databuilder = databuilder.addField(columns[i], data[i], true); //add data to embed
		
		return databuilder.build();
	}
	
	//messageembed emulates a row of data, fields and values represent columns and values of columns
	private static MessageEmbed createDataRow(Table table, String[] data)
	{
		EmbedBuilder databuilder = new EmbedBuilder()
				.setColor(65280)
				.setTitle("Data Entry:")
				.setTimestamp(Instant.now());
		
		//should already have verified that columns array is the same length as data array
		for(int i = 0; i < data.length; i++)
			databuilder = databuilder.addField(table.getColumns()[i], data[i], true); //add data to embed
		
		return databuilder.build();
	}
	
	//find the correct row with the specified value (first one we find) (CASE SENSITIVE)
	//TODO: return all rows with the specified value
	//if you really wanna use lambda expressions, you can use a chain of booleans to ride the kick up early
	public Message get(String column, String value)
	{
		//find the correct embed with the specified value (first one we find)
		for(Message datarowentry : textchannel.getIterableHistory())
		{
			for(MessageEmbed embed : datarowentry.getEmbeds())
			{
				for(Field datacolumn : embed.getFields())
				{
					if(datacolumn.getName().equals(column) && datacolumn.getValue().equals(value))
						return datarowentry;
				}
			}
		}
		
		return null;
	}
	
	//find the correct row with the specified value (first one we find) (CASE SENSITIVE)
	//TODO: return all rows with the specified value
	//if you really wanna use lambda expressions, you can use a chain of booleans to ride the kick up early
	public static Message get(Table table, String column, String value)
	{
		//find the correct embed with the specified value (first one we find)
		for(Message datarowentry : table.getTextChannel().getIterableHistory())
		{
			for(MessageEmbed embed : datarowentry.getEmbeds())
			{
				for(Field datacolumn : embed.getFields())
				{
					if(datacolumn.getName().equals(column) && datacolumn.getValue().equals(value))
						return datarowentry;
				}
			}
		}
		
		return null;
	}
	
	//delete a row from table by specifying a value of a column
	//TODO: delete all rows with the specified values
	public void delete(String column, String value)
	{
		get(this, column, value).delete().queue();
	}
	
	//delete a row from table by specifying a value of a column
	//TODO: delete all rows with the specified values
	public static void delete(Table table, String column, String value)
	{
		get(table, column, value).delete().queue();
	}
	
	//edit a row of data in a table by specifying the column and value
	//TODO: edit all rows with the specified values
	public void edit(String column, String oldvalue, String newvalue)
	{
		Message message = get(this, column, oldvalue);
		
		for(MessageEmbed olddata : message.getEmbeds())
		{
			EmbedBuilder dataeditor = new EmbedBuilder(olddata).clearFields();
			for(Field value : olddata.getFields())
				dataeditor = dataeditor.addField(value.getName(), value.getName().equals(column) ? newvalue : oldvalue, true);
		}
	}
	
	//edit a row of data in a table by specifying the column and value
	//TODO: edit all rows with the specified values
	public static void edit(Table table, String column, String oldvalue, String newvalue)
	{
		Message message = get(table, column, oldvalue);
		
		for(MessageEmbed olddata : message.getEmbeds())
		{
			EmbedBuilder dataeditor = new EmbedBuilder(olddata).clearFields();
			for(Field value : olddata.getFields())
				dataeditor = dataeditor.addField(value.getName(), value.getName().equals(column) ? newvalue : oldvalue, true);
		}
	}
	
	public TextChannel getTextChannel()
	{
		return this.textchannel;
	}
	
	public Guild getGuild()
	{
		return this.textchannel.getGuild();
	}
	
	public String[] getColumns()
	{
		return this.columns;
	}
}
