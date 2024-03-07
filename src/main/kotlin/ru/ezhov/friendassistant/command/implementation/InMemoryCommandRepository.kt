package ru.ezhov.friendassistant.command.implementation

import ru.ezhov.friendassistant.command.Command
import ru.ezhov.friendassistant.command.CommandRepository

class InMemoryCommandRepository : CommandRepository {
    override fun all(): List<Command> =
        listOf(
            BackupActionCommand(),
            BackupPasswordCommand(),
            BoardCommand(),
            BranchCommand(),
            JiraTaskCommand(),
            SearchCommand(),
            ShowErrorsCommand(),
            MailActionCommand(),
            CleanTextFromBufferCommand(),
        )
}
