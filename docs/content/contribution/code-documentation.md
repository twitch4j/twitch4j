+++
title="Code Documentation"
weight = 70
+++

# Code Documentation

The code of the project should be annotated with javadoc, so that we can create a automatic html docuementation and give helpful hints to users.

When writing javadoc comments please follow this guideline:

 - Punctuate every class and method description (sentence or phrase) with a period.
 - Starting with an action word, describe what each class and method does.
 - Avoid just restating the class or method name (e.g., Avoid updateLocalization(...) → Updates the localization OR @param key the key).
 - Describe the most important details in the first sentence, because it is the only one shown in the class/method summary.
 - Include all relevant tags (@param, @return, etc) for each method; without them, the method Javadoc is incomplete.
 - Start a method description with Returns, if the method returns a value.
 - Don’t explicitly refer to collections in descriptions (e.g., list of articles). Use, instead, the plural (e.g., the articles) or “all the” (e.g., all the articles).
 - When referring to another class, create a link by using {@link entity} (e.g., {@link TwitchClient}) 
 - Begin boolean parameter descriptions with whether.
 - Describe exceptions (e.g., @throws) in past tense.

## Examples

### Class

```java
/**
 * Short Descriptions
 * <p>
 * Detailed Description
 *
 * @author First Person
 * @author Second Person
 * @version %I%, %G%
 * @since 1.0
 */
public class ...
```

### Method

```java
/**
 * Returns a list of users who follow a specified channel, sorted by the date when they started following the channel (newest first, unless specified otherwise).
 * <p>
 * Requires Scope: none
 *
 * @param limit     Maximum number of most-recent objects to return (users who started following the channel most recently). Default: 25. Maximum: none.
 * @param direction Direction of sorting. Valid values: asc (oldest first), desc (newest first). Default: desc.
 * @return Returns all followers.
 */
```
