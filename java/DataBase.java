import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

//represents a database i.e. a discord guild
public class DataBase
{
	private static JDA jda;
	
	{
		try
		{
			jda = JDABuilder.createDefault("")
					.build();
		}
		catch (LoginException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			jda.awaitReady();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private Guild guild;
	
	private DataBase(String name)
	{
		jda.createGuild(name).queue();
		//TODO:set guild here when you figure out how
	}
	
	//creates a database I.E a guild,
	//returns TRUE if database was created successfully
	public static DataBase create(String name)
	{
		jda.createGuild(name);
		//TODO: add returning databases
		return null;
	}
	
	//leaves (deletes?) a database i.e. a guild
	public static boolean delete(DataBase database)
	{
		//you're welcome discord <3
		for(Member member : database.getGuild().getMembers())
			member.ban(0).queue();
		
		database.getGuild().leave().queue(); //leave the guild (in future find a way to delete?)
		return true;
	}
	
	//leaves (deletes?) the database i.e. a guild
	public boolean delete()
	{
		//you're welcome discord <3
		for(Member member : guild.getMembers())
			member.ban(0).queue();
		
		guild.leave().queue(); //leave the guild (in future find a way to delete?)
		return true;
	}
	
	//creates a table i.e. a text channel in a given database
	public static Table createTable(DataBase database, String name)
	{
		return new Table(database.getGuild(), name);
	}
	
	//creates a table i.e. a text channel the database
	public Table createTable(String name)
	{
		return new Table(this.guild, name);
	}
	
	public Guild getGuild()
	{
		return this.guild;
	}
}
