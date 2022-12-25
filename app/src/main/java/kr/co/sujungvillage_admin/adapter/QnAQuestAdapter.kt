package kr.co.sujungvillage_admin.adapter // ktlint-disable package-name

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kr.co.sujungvillage_admin.QnaDetailActivity
import kr.co.sujungvillage_admin.R
import kr.co.sujungvillage_admin.data.QuestionGetResultDTO
import kr.co.sujungvillage_admin.databinding.ListitemQnaMyqBinding

class QnAQuestionAdapter : RecyclerView.Adapter<QnAMyqHolder>() {
    var questionList = mutableListOf<QuestionGetResultDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QnAMyqHolder {
        val binding =
            ListitemQnaMyqBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return QnAMyqHolder(binding)
    }

    override fun onBindViewHolder(holder: QnAMyqHolder, position: Int) {
        val question = questionList.get(position)
        holder.setQuestion(question)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}

class QnAMyqHolder(val binding: ListitemQnaMyqBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setQuestion(question: QuestionGetResultDTO) {
        binding.textTitle.text = question.title
        binding.textState.text = if (question.isAnswered) "답변 완료" else "미답변"
        if (question.isAnswered) binding.textState.setBackgroundResource(R.drawable.style_qna_listitem_complete)
        else binding.textState.setBackgroundResource(R.drawable.style_qna_listitem_incomplete)

        // 내 질문 클릭 시 상세 액티비티 생성
        binding.root.setOnClickListener {
            var intent = Intent(binding.root.context, QnaDetailActivity::class.java)
            intent.putExtra("questionId", question.id)
            startActivity(binding.root.context, intent, null)
        }
    }
}
