package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.tag_already_taken

class UniqueTagRule(
    private val repository: TagRepository = inject(),
    private val localization: Localization = inject(),
) : ValidationRule<Tag> {

    override fun check(input: Tag): ValidationResult<Tag> {
        return when (repository.getByName(input.name)) {
            null -> ValidationResult.Success(input)
            else -> ValidationResult.Failure(input, error = localization.getString(Res.string.tag_already_taken))
        }
    }
}