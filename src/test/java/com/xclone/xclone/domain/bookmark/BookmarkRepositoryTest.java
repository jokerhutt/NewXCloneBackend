package com.xclone.xclone.domain.bookmark;
import com.xclone.xclone.helpers.JpaTestFactory;
import com.xclone.xclone.utils.TestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import java.sql.Timestamp;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaTestFactory.class)
class BookmarkRepositoryTest {

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    JpaTestFactory factory;

    @Test
    void findPaginatedBookmarkedPostIdsByTime_ordersDescAndPaginates() {
        var author = factory.persistDefaultUser();

        var post1 = factory.persistDefaultPost(author);
        var post2 = factory.persistSecondPost(author);
        var post3 = factory.persistThirdPost(author);

        factory.persistBookmark(author.getId(), post1.getId(), TestConstants.TIME_1);
        factory.persistBookmark(author.getId(), post2.getId(), TestConstants.TIME_2);
        factory.persistBookmark(author.getId(), post3.getId(), TestConstants.TIME_3);

        var cursor = Timestamp.from(TestConstants.CURSOR_TIME);
        var page = PageRequest.of(0, 2);

        var result = bookmarkRepository.findPaginatedBookmarkedPostIdsByTime(
                author.getId(), cursor, page);

        assertThat(result).containsExactly(post3.getId(), post2.getId());

    }
}
