package razerdp.friendcircle.mvp.view;

import java.util.List;

import razerdp.friendcircle.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.mvp.model.entity.UserInfo;
import razerdp.friendcircle.ui.widget.commentwidget.CommentWidget;

/**
 * Created by 大灯泡 on 2016/12/7.
 */

public interface IMomentView extends IBaseView {


    void onLikeChange(int itemPos, List<UserInfo> likeUserList);


    void onCommentChange(int itemPos, List<CommentInfo> commentInfoList);

    void showCommentBox(int itemPos, String momentid, CommentWidget commentWidget);

}
