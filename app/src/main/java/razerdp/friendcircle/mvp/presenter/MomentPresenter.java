package razerdp.friendcircle.mvp.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobObject;
import razerdp.friendcircle.app.manager.LocalHostManager;
import razerdp.friendcircle.mvp.callback.OnCommentChangeCallback;
import razerdp.friendcircle.mvp.callback.OnLikeChangeCallback;
import razerdp.friendcircle.mvp.model.CommentImpl;
import razerdp.friendcircle.mvp.model.LikeImpl;
import razerdp.friendcircle.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.mvp.model.entity.UserInfo;
import razerdp.friendcircle.mvp.view.IMomentView;
import razerdp.friendcircle.utils.ToolUtil;
import razerdp.friendcircle.ui.widget.commentwidget.CommentWidget;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * 朋友圈presenter
 */

public class MomentPresenter implements IMomentPresenter {
    private IMomentView momentView;
    private CommentImpl commentModel;
    private LikeImpl likeModel;

    public MomentPresenter() {
        this(null);
    }

    public MomentPresenter(IMomentView momentView) {
        this.momentView = momentView;
        commentModel = new CommentImpl();
        likeModel = new LikeImpl();
    }

    @Override
    public IBasePresenter<IMomentView> bindView(IMomentView view) {
        this.momentView = view;
        return this;
    }

    @Override
    public IBasePresenter<IMomentView> unbindView() {
        return this;
    }

    //=============================================================动作控制
    @Override
    public void addLike(final int viewHolderPos, String momentid, final List<UserInfo> currentLikeUserList) {
        likeModel.addLike(momentid, new OnLikeChangeCallback() {
            @Override
            public void onLike() {
                List<UserInfo> resultLikeList = new ArrayList<UserInfo>();
                if (!ToolUtil.isListEmpty(currentLikeUserList)) {
                    resultLikeList.addAll(currentLikeUserList);
                }
                boolean hasLocalLiked = findPosByObjid(resultLikeList, LocalHostManager.INSTANCE.getUserid()) > -1;
                if (!hasLocalLiked) {
                    resultLikeList.add(0, LocalHostManager.INSTANCE.getLocalHostUser());
                }
                if (momentView != null) {
                    momentView.onLikeChange(viewHolderPos, resultLikeList);
                }
            }

            @Override
            public void onUnLike() {

            }

        });
    }

    @Override
    public void unLike(final int viewHolderPos, String momentid, final List<UserInfo> currentLikeUserList) {
        likeModel.unLike(momentid, new OnLikeChangeCallback() {
            @Override
            public void onLike() {

            }

            @Override
            public void onUnLike() {
                List<UserInfo> resultLikeList = new ArrayList<UserInfo>();
                if (!ToolUtil.isListEmpty(currentLikeUserList)) {
                    resultLikeList.addAll(currentLikeUserList);
                }
                final int localLikePos = findPosByObjid(resultLikeList, LocalHostManager.INSTANCE.getUserid());
                if (localLikePos > -1) {
                    resultLikeList.remove(localLikePos);
                }
                if (momentView != null) {
                    momentView.onLikeChange(viewHolderPos, resultLikeList);
                }
            }

        });
    }

    @Override
    public void addComment(final int viewHolderPos, String momentid, String replyUserid, String commentContent, final List<CommentInfo> currentCommentList) {
        if (TextUtils.isEmpty(commentContent)) return;
        commentModel.addComment(momentid, LocalHostManager.INSTANCE.getUserid(), replyUserid, commentContent, new OnCommentChangeCallback() {
            @Override
            public void onAddComment(CommentInfo response) {
                List<CommentInfo> resultLikeList = new ArrayList<CommentInfo>();
                if (!ToolUtil.isListEmpty(currentCommentList)) {
                    resultLikeList.addAll(currentCommentList);
                }
                resultLikeList.add(response);
                KLog.i("comment", "评论成功 >>>  " + response.toString());
                if (momentView != null) {
                    momentView.onCommentChange(viewHolderPos, resultLikeList);
                }

            }

            @Override
            public void onDeleteComment(String response) {

            }
        });
    }

    @Override
    public void deleteComment(final int viewHolderPos, String commentid, final List<CommentInfo> currentCommentList) {
        if (TextUtils.isEmpty(commentid)) return;
        commentModel.deleteComment(commentid, new OnCommentChangeCallback() {
            @Override
            public void onAddComment(CommentInfo response) {

            }

            @Override
            public void onDeleteComment(String commentid) {
                if (TextUtils.isEmpty(commentid)) return;
                List<CommentInfo> resultLikeList = new ArrayList<CommentInfo>();
                if (!ToolUtil.isListEmpty(currentCommentList)) {
                    resultLikeList.addAll(currentCommentList);
                }
                Iterator<CommentInfo> iterator = resultLikeList.iterator();
                while (iterator.hasNext()) {
                    CommentInfo info = iterator.next();
                    if (TextUtils.equals(info.getCommentid(), commentid)) {
                        iterator.remove();
                        break;
                    }
                }
                KLog.i("comment", "删除评论成功 >>>  " + commentid);
                if (momentView != null) {
                    momentView.onCommentChange(viewHolderPos, resultLikeList);
                }

            }
        });

    }


    public void showCommentBox(int itemPos, String momentid, @Nullable CommentWidget commentWidget) {
        if (momentView != null) {
            momentView.showCommentBox(itemPos, momentid, commentWidget);
        }
    }


    /**
     * 从bmobobj列表寻找符合id的位置
     *
     * @return -1:找不到
     */
    private int findPosByObjid(List<? extends BmobObject> objectList, String id) {
        int result = -1;
        if (ToolUtil.isListEmpty(objectList)) return result;
        for (int i = 0; i < objectList.size(); i++) {
            BmobObject object = objectList.get(i);
            if (TextUtils.equals(object.getObjectId(), id)) {
                result = i;
                break;
            }
        }
        return result;
    }


    //------------------------------------------interface impl-----------------------------------------------
}
